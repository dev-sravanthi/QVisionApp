package util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;


public class MyCustomEditText  extends AppCompatEditText {

    public MyCustomEditText(Context context, AttributeSet attr){
        super(context,attr);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "Sansation-Regular.ttf"));
    }
}