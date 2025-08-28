/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Validaciones;
import javax.swing.text.DefaultFormatter;
import java.lang.reflect.Method;
import java.text.ParseException;

/**
 *
 * @author Eduard Salas Murillo
 */

public class CustomRegexFormatter extends DefaultFormatter {

    private String regex;
    private String errorMessage;

    public CustomRegexFormatter(String regex, String errorMessage) {
        this.regex = regex;
        this.errorMessage = errorMessage;
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }

        if (!text.matches(regex)) {
            throw new ParseException(errorMessage, 0);
        }
        return text;
    }
}