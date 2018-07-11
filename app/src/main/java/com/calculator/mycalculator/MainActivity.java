package com.calculator.mycalculator;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.calculator.mycalculator.model.Constantes;
import com.calculator.mycalculator.model.Metodos;
import com.calculator.mycalculator.model.OnResolveCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnClear)
    Button btnClear;
    @BindView(R.id.btnDiv)
    Button btnDiv;
    @BindView(R.id.btnMultiplication)
    Button btnMultiplication;
    @BindView(R.id.btnSub)
    Button btnSub;
    @BindView(R.id.btnSum)
    Button btnSum;
    @BindView(R.id.btnResult)
    Button btnResult;
    private Context context;
    @BindView(R.id.etInput)
    EditText etInput;
    @BindView(R.id.contentMain)
    RelativeLayout contentMain;
    @BindView(R.id.btnSeven)
    Button btnSeven;
    @BindView(R.id.btnFour)
    Button btnFour;
    @BindView(R.id.btnOne)
    Button btnOne;
    @BindView(R.id.btnPoint)
    Button btnPoint;
    @BindView(R.id.btnEight)
    Button btnEight;
    @BindView(R.id.btnFive)
    Button btnFive;
    @BindView(R.id.btnTwo)
    Button btnTwo;
    @BindView(R.id.btnZero)
    Button btnZero;
    @BindView(R.id.btnNine)
    Button btnNine;
    @BindView(R.id.btnSix)
    Button btnSix;
    @BindView(R.id.btnThree)
    Button btnThree;

    private boolean isEditinProgrees=false;
    private int minLength;
    private int textSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;
        //Asignar el tamaño de la letra dependiendo del dispositivo
        minLength = getResources().getInteger(R.integer.main_min_length);
        textSize = getResources().getInteger(R.integer.main_input_textSize);
        configEditText();
    }

    public void configEditText() {
        //Metodo que sirve para que evitar que salga el teclado en el editext cuando se inicia
        etInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        //Metodo para borrar elemento del input
        etInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    //si la posicion del evento touch
                        if(motionEvent.getRawX() >= (etInput.getRight())
                                -etInput.getCompoundDrawables()[Constantes.DRAWABLE_RIGHT].getBounds().width()
                                ){
                            if(etInput.length() >0){
                                final int length=etInput.getText().length();
                                etInput.getText().delete(length-1,length);
                            }
                        }
                        return false;
                }
                return false;
            }
        });

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Cambiar texto en la operacion
                if (!isEditinProgrees && Metodos.canReplaceOperator(charSequence)){
                    isEditinProgrees=true;
                    etInput.getText().delete(etInput.getText().length()-2, etInput.getText().length()-1);
                }

                if (charSequence.length() > minLength) {
                    etInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize -
                            (((charSequence.length() - minLength) * 2) + (charSequence.length() - minLength)));
                } else {
                    etInput.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                   isEditinProgrees=false;
            }
        });
    }

    @OnClick({R.id.btnSeven, R.id.btnFour, R.id.btnOne, R.id.btnPoint, R.id.btnEight, R.id.btnFive,
            R.id.btnTwo, R.id.btnZero, R.id.btnNine, R.id.btnSix, R.id.btnThree})
    public void onViewClicked(View view) {
        /*
         * obtiene el texto del boton
         * */
        final String valStr = ((Button) view).getText().toString();
        switch (view.getId()) {
            case R.id.btnSeven:
            case R.id.btnFour:
            case R.id.btnOne:
            case R.id.btnThree:
            case R.id.btnEight:
            case R.id.btnFive:
            case R.id.btnTwo:
            case R.id.btnZero:
            case R.id.btnSix:
            case R.id.btnNine:
                //append Sirve para añadir el texto a edittex
                etInput.getText().append(valStr);
                break;
            case R.id.btnPoint:
                final String operacion = etInput.getText().toString();
                final String operador = Metodos.getOperator(operacion);
                final int count = operacion.length() - operacion.replace(".", "").length();

                if (!operacion.contains(Constantes.POINT) || (count < 2 && (!operacion.equals(Constantes.OPERATOR_NULL)))) {
                    etInput.getText().append(valStr);
                }

                break;
        }
    }

    @OnClick({R.id.btnClear, R.id.btnDiv, R.id.btnMultiplication, R.id.btnSub, R.id.btnSum, R.id.btnResult})
    public void onClickControl(View view) {
        switch (view.getId()) {
            case R.id.btnClear:
                etInput.setText("");
                break;
            case R.id.btnDiv:
            case R.id.btnMultiplication:
            case R.id.btnSub:
            case R.id.btnSum:
                result(false);
                final String operador=((Button)view).getText().toString();
                final String operacion=etInput.getText().toString();
                final String ultimoCaracter=operacion.isEmpty()? "": operacion.substring(operacion.length()-1);
                if (operador.equals(Constantes.OPERATOR_SUB)) {
                    if (operacion.isEmpty() ||
                            (!(ultimoCaracter.equals(Constantes.OPERATOR_SUB)) &&
                                    !(ultimoCaracter.equals(Constantes.POINT)))) {
                        etInput.getText().append(operador);
                    }
                } else {
                    if (!operacion.isEmpty() &&
                            !(ultimoCaracter.equals(Constantes.OPERATOR_SUB)) &&
                            !(ultimoCaracter.equals(Constantes.POINT))) {
                        etInput.getText().append(operador);
                    }
                }
                break;
            case R.id.btnResult:
                result(false);

                break;
        }
    }

    private void result(boolean fromresult ){
        Metodos.tryResolve(fromresult, etInput, new OnResolveCallback() {
            @Override
            public void onShowMessage(int errorRes) {
                showMessage(errorRes);
            }

            @Override
            public void onIsEditing() {
                isEditinProgrees=true;
            }
        });

    }

    private void showMessage(int error){
        Snackbar.make(contentMain,error,Snackbar.LENGTH_LONG).show();
    }
}
