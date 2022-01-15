package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static java.util.Map.entry;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * User-specified options influencing the compilation.
 *
 * @author gl60
 * @date 01/01/2022
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;

    private static final Map<String, OptionData> options = Map.ofEntries(
        entry("-b", new OptionData("banner", 
            "affiche une bannière indiquant le nom de l'équipe", 0)),
        entry("-P", new OptionData("parallel", 
            "s'il y a plusieurs fichiers sources, lance la compilation " +
            "des fichiers en parallèle (pour accélérer la compilation)", 0)),
        entry("-d", new OptionData("debug", 
            "active les traces de debug. Répéter l'option plusieurs fois " +
            "pour avoir plus de traces.", 0)),
        entry("-h", new OptionData("help", "Affiche cette aide", 0)),
        entry("-p", new OptionData("decompilation", "decompile l'arbre" +
            " abstrait obtenu à la suite du lexer et du parser" , 0)),
        entry("-r", new OptionData("registers", "limite les registres " +
            "banalisés disponibles. Doit être compris entre 4 et 16 inclus."
            , 1))
    );

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();

    public int getDebug() {
        return options.get("-d").getNbInvoked();
    }

    public boolean getParallel() {
        return options.get("-P").isInvoked();
    }

    public boolean getPrintBanner() {
        return options.get("-b").isInvoked();
    }

    public boolean getHelp() {
        return options.get("-h").isInvoked();
    }

    public boolean getDecompile() {
        return options.get("-p").isInvoked();
    }

    public int getNbReg() {
        if (options.get("-r").getArgs() != null) {
            int nbReg = Integer.parseInt(options.get("-r").getArgs()[0]);
            if (nbReg < 4 || nbReg > 16) {
                throw new UnsupportedOperationException("Can't use " + nbReg
                    + " registers: we need between 4 and 16 registers.");
            }
            return nbReg;
        }
        return 16;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public void parseArgs(String[] args) throws CLIException {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            OptionData option = options.get(arg);
            if (option != null) {
                String[] optArgs = new String[option.nbArgs];
                /* boucle qui, pour chaque argument requis, prend le prochain 
                 * argument, et met i à l'indice de ce nouvel argument */
                for (int j = 0; j < option.nbArgs; j++) {
                    optArgs[j] = args[++i];
                }
                option.invoke(optArgs);
            } else {
                sourceFiles.add(new File(arg));
            }
        }
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
    }

    protected void displayUsage() {
        System.out.println("USAGE: decac [OPTIONS] file");
        System.out.println("DECAC is a deca compiler.");
        System.out.println("");
        System.out.println("The argument `file` should be a file," +
                           " unless stated otherwise by a used option.");
        System.out.println("");
        System.out.println("Available options:");
        for (String optionName : options.keySet()) {
            OptionData option = options.get(optionName);
            String profile = "  " + optionName + "  (" + option.name + ")\t: ";
            int indent = profile.length();
            System.out.print(profile);
            System.out.println(option.description);
        }
    }
}

class OptionData {
    public final String name;
    public final String description;
    public final int nbArgs;
    private int nbInvoke;
    private String[] args;
    
    public OptionData(String name, String description, int nbArgs) {
        this.name = name;
        this.description = description;
        this.nbInvoke = 0;
        this.nbArgs = nbArgs;
    }

    public boolean isInvoked() {
        return nbInvoke > 0;
    }

    public String[] getArgs() {
        return this.args;
    }

    public void invoke(String[] args) {
        nbInvoke++;
        this.args = args;
    }

    public int getNbInvoked() {
        return nbInvoke;
    }
}
