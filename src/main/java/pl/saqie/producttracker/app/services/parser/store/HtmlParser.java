package pl.saqie.producttracker.app.services.parser.store;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface HtmlParser {

    Document getDocumentFromUrl(String url) throws IOException;

}
