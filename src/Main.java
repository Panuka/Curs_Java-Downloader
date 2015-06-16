import org.apache.commons.cli.*;
import org.apache.commons.io.FileDeleteStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.EnumMap;
import java.util.concurrent.TimeUnit;

public class Main {

	
    public static void main(String[] args) throws ParseException {
        //-n 5 -l 2000k -o output_folder -f links.txt
        
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
        int nv = 2;
        
        System.out.print("Введите текущую версию приложения ");
        cv = Integer.parseInt(System.console().readLine());
        
        System.out.print("Введите номер необходимой версии приложения ");
        nv = Integer.parseInt(System.console().readLine());
        long startTime = System.currentTimeMillis();

        if (line.hasOption("n")) {
            paramsHash.put(Flags.countThreads, "5");
        }
        if (line.hasOption("l")) {
            paramsHash.put(Flags.downloadLimit, "2000k");
        }
        if (line.hasOption("o")) {
            paramsHash.put(Flags.outputFolder, "./");
        }
        if (line.hasOption("f")) {
            paramsHash.put(Flags.linksFile, "links.txt");
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
        try {
            System.out.println("Программа успешно обновлена до версии "+nv+"!");
            System.out.println("Для продолжения нажмите Enter...");
			System.in.read();
	        File links_file = new File(System.getProperty("user.dir")+"\\links.txt");
	        FileDeleteStrategy.FORCE.delete(links_file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
