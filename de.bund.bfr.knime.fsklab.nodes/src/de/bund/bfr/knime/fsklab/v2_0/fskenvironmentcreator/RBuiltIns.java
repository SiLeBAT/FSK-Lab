package de.bund.bfr.knime.fsklab.v2_0.fskenvironmentcreator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public final class RBuiltIns {

    /** R packages with Priority “base” or “recommended” (R ≥ 4.4). */
    private static final Set<String> BUILT_INS = Collections.unmodifiableSet(
        new TreeSet<>(Arrays.asList(
            // base (15)
            "base", "compiler", "datasets", "graphics", "grDevices",
            "grid", "methods", "parallel", "splines", "stats",
            "stats4", "tcltk", "tools", "utils", "translations",
            // recommended (15)
            "boot", "class", "cluster", "codetools", "foreign",
            "KernSmooth", "lattice", "MASS", "Matrix", "mgcv",
            "nlme", "nnet", "rpart", "spatial", "survival"
        ))
    );

    /** Returns an unmodifiable, sorted view of the built‑in package names. */
    public static Set<String> builtIns() {
        return BUILT_INS;
    }

    private RBuiltIns() { /* utility class */ }
}
