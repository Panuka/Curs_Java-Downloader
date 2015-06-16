import org.apache.commons.cli.*;

import java.util.EnumMap;
import java.util.concurrent.TimeUnit;

public class Main {

	
    public static void main(String[] args) throws ParseException {
        //-n 5 -l 2000k -o output_folder -f links.txt
        long startTime = System.currentTimeMillis();
        
//        
        String domain = "http://updater.ru/ver/";
        String version = "1";

        Option countThreadsOption = new Option("n", "count", true, "");
        Option downloadLimitOption = new Option("l", "limit", true, "");
        Option outputFolderOption = new Option("o", "output_folder", true, "");
        Option linksFileOption = new Option("f", "linksFile", true, "");
        Option currVer = new Option("cv", "currVer", true, "");
        Option needVer = new Option("nv", "needVer", true, "");
        Options options = new Options();
        
        options.addOption(countThreadsOption);
        options.addOption(downloadLimitOption);
        options.addOption(outputFolderOption);
        options.addOption(linksFileOption);
        options.addOption(needVer);
        options.addOption(currVer);

        CommandLineParser parser = new PosixParser();
        CommandLine line = parser.parse(options, args);
        EnumMap<Flags, String> paramsHash = new EnumMap<Flags, String>(Flags.class);
        int cv = 1;
        int nv = 1;
        if (line.hasOption("n")) {
            paramsHash.put(Flags.countThreads, line.getOptionValue("n"));
        }
        if (line.hasOption("l")) {
            paramsHash.put(Flags.downloadLimit, line.getOptionValue("l"));
        }
        if (line.hasOption("o")) {
            paramsHash.put(Flags.outputFolder, line.getOptionValue("o"));
        }
        if (line.hasOption("f")) {
            paramsHash.put(Flags.linksFile, line.getOptionValue("f"));
        }
        if (line.hasOption("cv")) {
        	cv = Integer.parseInt(line.getOptionValue("cv"));
        }
        if (line.hasOption("nv")) {
        	nv = Integer.parseInt(line.getOptionValue("nv"));
        }
        String url = domain+"make.php?cv="+cv+"&nv="+nv;
    	Downloader list = new Downloader(url, "links.txt", System.getProperty("user.dir"));
    	list.run();
        startDownload(paramsHash);

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(totalTime),
                TimeUnit.MILLISECONDS.toSeconds(totalTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalTime))
        ));
    }

    public static void startDownload(EnumMap<Flags, String> params) {
        Core core = new Core(params);
        try {
            core.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
