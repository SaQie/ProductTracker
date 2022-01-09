package pl.saqie.producttracker.app.services.parser.store;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JSoupHtmlParser implements HtmlParser {

    @Override
    public Document getDocumentFromUrl(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
