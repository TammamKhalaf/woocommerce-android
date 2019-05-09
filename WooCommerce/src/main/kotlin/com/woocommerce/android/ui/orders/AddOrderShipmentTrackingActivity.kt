package com.woocommerce.android.ui.orders

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.woocommerce.android.R
import com.woocommerce.android.analytics.AnalyticsTracker
import com.woocommerce.android.tools.NetworkStatus
import com.woocommerce.android.ui.base.UIMessageResolver
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_add_shipment_tracking.*
import org.wordpress.android.fluxc.utils.DateUtils
import java.util.Calendar
import javax.inject.Inject

class AddOrderShipmentTrackingActivity : AppCompatActivity(), AddOrderShipmentTrackingContract.View {
    companion object {
        const val FIELD_ORDER_TRACKING_NUMBER = "order-tracking-number"
        const val FIELD_ORDER_TRACKING_DATE_SHIPPED = "order-tracking-date-shipped"
        const val FIELD_ORDER_TRACKING_PROVIDER = "order-tracking-provider"
    }

    @Inject lateinit var networkStatus: NetworkStatus
    @Inject lateinit var uiMessageResolver: UIMessageResolver
    @Inject lateinit var presenter: AddOrderShipmentTrackingContract.Presenter

    private var dateShippedPickerDialog: DatePickerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_shipment_tracking)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_gridicons_cross_white_24dp)
        title = getString(R.string.order_shipment_tracking_toolbar_title)

        if (savedInstanceState != null) {
            addTracking_number.setText(savedInstanceState.getString(FIELD_ORDER_TRACKING_NUMBER, ""))
            addTracking_editCarrier.text = savedInstanceState.getString(FIELD_ORDER_TRACKING_PROVIDER, "")
            addTracking_date.text = savedInstanceState.getString(FIELD_ORDER_TRACKING_DATE_SHIPPED)
        } else {
            displayFormatDateShippedText(DateUtils.getCurrentDateString())
            // TODO: if user has selected a previous provider then that value should be displayed
        }

        /**
         *
         * When date field is clicked, open calendar dialog with default date set to
         * current date if no date was previously selected
         */
        addTracking_date.setOnClickListener {
            val calendar = DateUtils.getCalendarInstance(getDateShippedText())
            dateShippedPickerDialog = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val actualMonth = month + 1
                        displayFormatDateShippedText(String.format("$year-$actualMonth-$dayOfMonth"))
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
            )
            dateShippedPickerDialog?.show()
        }

        addTracking_editCarrier.setOnClickListener {
            // open list dialog fragment with list of providers
        }

        presenter.takeView(this)
    }

    override fun onPause() {
        super.onPause()
        /**
         * If calendar dialog is displaying when activity is in a paused state,
         * then dismiss the dialog
         */
        dateShippedPickerDialog?.dismiss()
        dateShippedPickerDialog = null
    }

    override fun onResume() {
        super.onResume()
        AnalyticsTracker.trackViewShown(this)
    }

    override fun onDestroy() {
        presenter.dropView()
        super.onDestroy()
    }

    /**
     * Reusing the same menu used for adding order notes
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_add -> {
                // TODO: add button functionality to be added
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(FIELD_ORDER_TRACKING_NUMBER, addTracking_number.text.toString())
        outState?.putString(FIELD_ORDER_TRACKING_DATE_SHIPPED, addTracking_date.text.toString())
        outState?.putString(FIELD_ORDER_TRACKING_PROVIDER, addTracking_editCarrier.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun getDateShippedText(): String {
        // format displayed date to YYY-MM-dd i.e. from May 9, 2019 -> 2019-05-09
        return com.woocommerce.android.util.DateUtils.getDateString(addTracking_date.text.toString())
    }

    private fun displayFormatDateShippedText(dateString: String) {
        addTracking_date.text = com.woocommerce.android.util.DateUtils.getLocalizedLongDateString(
                applicationContext,
                dateString
        )
    }
}
