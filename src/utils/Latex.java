package utils;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class Latex {
    public static TeXIcon convert(String f) {
        TeXFormula formula = new TeXFormula(f);

        return formula.createTeXIcon(
                TeXConstants.STYLE_DISPLAY,
                15
        );
    }

    public static TeXIcon convert(String f, int size) {
        TeXFormula formula = new TeXFormula(f);

        return formula.createTeXIcon(
                TeXConstants.STYLE_DISPLAY,
                size
        );
    }
}