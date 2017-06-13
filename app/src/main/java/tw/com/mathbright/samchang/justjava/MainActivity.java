package tw.com.mathbright.samchang.justjava;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.NumberFormat;

import static android.R.attr.name;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    private LinearLayout introMessage;
    private ScrollView appContent;

    private static String oderSummaryMsg;
    private static int numberOfCoffees = 1;
    private final static int PRICE_OF_COFFEE = 5;
    private final static int PRICE_OF_WHIPPED_CREAM = 1;
    private final static int PRICE_OF_CHOCOLATE = 2;
    private final static int MIN_ORDER = 1;
    private final static int MAX_ORDER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        introMessage = (LinearLayout) findViewById(R.id.welcome_message_layout);
        //appContent = (LinearLayout) findViewById(R.id.app_content_layout);
        appContent = (ScrollView) findViewById(R.id.scroll_layout);
        // Log.i("Activity.javaMain", "Just Java - onCreate");

        // prepare and display a toast message
//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.custom_toast,
//                (ViewGroup) findViewById(R.id.custom_toast_container));
//
//        TextView text = (TextView) layout.findViewById(R.id.toast_text);
//        text.setText("Welcome to Just Java!");
//        text.setTextColor(Color.GREEN);
//
//        Toast toast = new Toast(getApplicationContext());
//        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
//        toast.setDuration(Toast.LENGTH_LONG);
//        toast.setView(layout);
//        toast.show();
    }

    public void dismissWelcomeMessageBox(View view) {
        introMessage.setVisibility(View.GONE);
        //Log.v("MainActivity", "set introMessage INVISIBLE");
        //introMessage.invalidate();
        appContent.setVisibility(View.VISIBLE);
        //Log.v("MainActivity", "set appContent VISIBLE");
        //appContent.invalidate();

    }

    /**
     * Retrieve order information and then log it to the screen output
     *
     * @param view
     */
    public void printToLogs(View view) {
        // Find the order info TextView and print the text to the logs
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.chkCream);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chkChocolate);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        // retrieve the name string from EditText
        EditText nameText = (EditText) findViewById(R.id.customer_name);
        String name = nameText.getText().toString();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String summary = createOrderSummary(name, price, hasWhippedCream, hasChocolate);
        Log.i("Activity.javaMain", "[ JUST JAVA ]\n" + summary + "\nThank you!");
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.chkCream);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chkChocolate);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        // retrieve the name string from EditText
        EditText nameText = (EditText) findViewById(R.id.customer_name);
        String name = nameText.getText().toString();
        //Log.v("MainActivity", "Name: " + name);

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String summary = createOrderSummary(name, price, hasWhippedCream, hasChocolate);
        displayMessage(summary);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@mathbright.com.tw"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for: "+name);
        intent.putExtra(Intent.EXTRA_TEXT, summary);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
//        try {
//            startActivity(Intent.createChooser(intent, "Send mail..."));
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * Create a summary for order placed
     *
     * @param customerName
     * @param price
     * @return the summary string
     */
    private String createOrderSummary(String customerName, int price, boolean hasWhippedCream, boolean hasChocolate) {
        String priceMsg = getString(R.string.order_name, customerName);
        priceMsg += "\n" + getString(R.string.order_quantity, numberOfCoffees);
        if (hasWhippedCream) {
            priceMsg += "\n" + getString(R.string.whipped_cream_added);
        }
        if (hasChocolate) {
            priceMsg += "\n" + getString(R.string.chocolate_added);
        }
        priceMsg += "\n" + getString(R.string.order_total,
                NumberFormat.getCurrencyInstance().format(price));

        return priceMsg;
    }

    /**
     * Calculates the price of the order based on the current quantity.
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int totalPrice = numberOfCoffees * PRICE_OF_COFFEE;
        if (hasWhippedCream) {
            totalPrice += numberOfCoffees * PRICE_OF_WHIPPED_CREAM;
        }
        if (hasChocolate) {
            totalPrice += numberOfCoffees * PRICE_OF_CHOCOLATE;
        }
        return totalPrice;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    public void increment(View view) {
        if (numberOfCoffees >= MAX_ORDER) {
            Toast.makeText(this, getString(R.string.max_order_msg),
                    Toast.LENGTH_SHORT).show();
        } else {
            numberOfCoffees++;
            displayQuantity(numberOfCoffees);
        }
    }

    public void decrement(View view) {
        if (numberOfCoffees > MIN_ORDER) {
            numberOfCoffees--;
            displayQuantity(numberOfCoffees);
        } else { /* min order is 1 cup of coffee */
            Toast.makeText(this, getString(R.string.min_order_msg),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
