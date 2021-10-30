package it.maraschi.wistiareader;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;

@SpringBootApplication
@Slf4j
public class WistiaReaderApplication implements CommandLineRunner {

	@Value("classpath:starting-links.txt")
	Resource startingLinksResource;

	@Value("classpath:good-links.txt")
	Resource goodLinksResource;

	public static void main(String[] args) {
		SpringApplication.run(WistiaReaderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		WistiaVideoDownloader wistiaVideoDownloader = new WistiaVideoDownloader();
		FileInputStream startingLinks = new FileInputStream(startingLinksResource.getFile());
		BufferedReader startingLinkReader = new BufferedReader(new InputStreamReader(startingLinks));
		FileOutputStream goodLinks = new FileOutputStream(goodLinksResource.getFilename());
		BufferedWriter goodLinksWriter = new BufferedWriter(new OutputStreamWriter(goodLinks));

		String link = startingLinkReader.readLine();
		while ( link != null ) {
			String code = wistiaVideoDownloader.prepareVideoLink(link);
			Document doc = Jsoup.connect(Constants.WISTIA_URL_PREFIX + code).get();
			String title = doc.title();
			log.info("Titolo: {}", title);
//			log.info("URL finale: {}", Constants.WISTIA_URL_PREFIX + code);
			System.out.println(Constants.WISTIA_URL_PREFIX + code);
			goodLinksWriter.write(Constants.WISTIA_URL_PREFIX + code);
			goodLinksWriter.newLine();
			link = startingLinkReader.readLine();
		}
		goodLinksWriter.flush();
		goodLinksWriter.close();
//		wistiaVideoDownloader.requestVideo(title, Constants.WISTIA_URL_PREFIX + code);
	}

	public void wistiaReader() {
		log.info("Wistia Reader - start");
		int counter = 0;

		RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange('0', 'z')
				.filteredBy(CharacterPredicates.DIGITS, CharacterPredicates.ASCII_LOWERCASE_LETTERS).build();

		try {
			File goodStringsFile = new File("good-links.txt");
			FileWriter fw = new FileWriter(goodStringsFile, true);

			while (true) {
				String randomString = generator.generate(Constants.STRING_LENGTH);
				counter++;
				System.out.print("Counter: " + counter + " Random string: " + randomString + "\r");

				Document doc = Jsoup.connect(Constants.WISTIA_URL_PREFIX + randomString).get();
				String title = doc.title();
				Element body = doc.body();

				if (!title.equalsIgnoreCase(Constants.PAGE_TITLE_ERR)) {
					System.out.println();
					log.info("Using random string: {}", randomString);
					log.info("Page title: {}", title);
					fw.write(randomString + " - title: " + title + "\r\n");
					log.info("Page body: {} - {}", body, body.attr("id"));
					break;
				}
			}
			fw.close();

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		log.info("Wistia Reader - end");
	}
}
