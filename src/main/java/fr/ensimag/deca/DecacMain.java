package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl60
 * @date 01/01/2022
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (compilerOptions.getVerification() && compilerOptions.getDecompile()) {
            System.err.println("Error: options -v and -p are incompatible.");
            options.displayUsage();
            System.exit(1);
        }
        if (options.getHelp()) {
            options.displayUsage();
            System.exit(0);
        }
        if (options.getPrintBanner()) {
            System.out.println("=================================================");
            System.out.println("====           Projet GL groupe 60           ====");
            System.out.println("=================================================");
            System.exit(0);
        }
        if (options.getSourceFiles().isEmpty()) {
            options.displayUsage();
            System.exit(0);
        }
        if (options.getParallel()) {
            List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
            for (File source : options.getSourceFiles()) {
                Callable<Boolean> c = new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        DecacCompiler compiler = new DecacCompiler(options, source);
                        return new Boolean(compiler.compile());
                    }
                };
                tasks.add(c);
            }
            ExecutorService exec = Executors.newCachedThreadPool();
            List<Future<Boolean>> results = exec.invokeAll(tasks);
            for (Future<Boolean> future : results) {
                // if this error is true or a previous one is, stays at true
                error ||= future.get();
            }
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
