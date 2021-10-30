package it.maraschi.wistiareader;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class WistiaVideoDownloader {

    public WistiaVideoDownloader() {
    }

    public String prepareVideoLink(String htmlCode) throws MalformedURLException {
        String videoLink = "";

        log.info("HtmlCode: {}", htmlCode);
        Document doc = Jsoup.parseBodyFragment(htmlCode);
        Element body = doc.body();
        Elements links = body.getElementsByTag("a");
        log.info("Links: {}", links);
        if (!links.isEmpty()) {
            URL url = new URL(links.first().attr("href"));
            String query = url.getQuery();
            log.info("Query: {}", query);
            int idx = query.indexOf("=");
            videoLink = query.substring(idx + 1);
        }
        return videoLink;
    }

    public void requestVideo(String title, String urlVideo) throws IOException {
//        I usually do it using HtmlUnit. They have an example on their page :

        // TODO convert / into _ inside title
        final WebClient webClient = new WebClient();

        // Get the first page
//        final HtmlPage page1 = webClient.getPage("https://getvideo.at/en/");
//        log.info("Page getvideo: {}", page1);
        Document page1 = Jsoup.connect("https://getvideo.at/en/").get();
        log.info("Page getvideo: {}", page1.body().html());

        // Get the form that we are dealing with and within that form,
        // find the submit button and the field that we want to change.
//        final HtmlForm form = page1.getFormByName("form-search-video");
//
//        final HtmlSubmitInput button = form.getInputByName("search-button");
//        final HtmlTextInput textField = form.getInputByName("search-text");

        // Change the value of the text field
//        textField.setValueAttribute(urlVideo);

        // Now submit the form by clicking the button and get back the second page.
//        final HtmlPage pageWithVideos = button.click();

        // TODO search for anchor links with "download" attribute equal to title
//        List<HtmlAnchor> links = pageWithVideos.getAnchors().stream().filter(anchor ->
//                anchor.getAttribute("download").equalsIgnoreCase(title)).collect(Collectors.toList());
//        log.info("Links: {}", links);
        // TODO filter one has span class="badge pull-right" content equal to "720"
        // TODO download in a video file the anchor link of the choosen one

//            List<HtmlElement> listVideos = pageWithVideos.get
//                    .getElementsByAttribute("span", "class","list-group");
//            log.info("ListVideos: {}", listVideos);
//            Optional<DomNode> node = listVideos.stream().map(video -> video.getFirstChild()).filter(video -> video.getNodeType() == Node.TEXT_NODE)
//                    .filter(video -> video.getTextContent().trim().equalsIgnoreCase("720")).findFirst();
//            if ( node.isPresent() ) {
//                String videoLink = node.get().getTextContent();
//            }
    }
}
