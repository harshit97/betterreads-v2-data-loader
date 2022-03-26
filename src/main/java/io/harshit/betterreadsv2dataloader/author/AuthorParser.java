package io.harshit.betterreadsv2dataloader.author;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class AuthorParser {

    public AuthorById getAuthorByIdFromLine(String authorLine) {
        if (authorLine == null || authorLine.isEmpty()) {
            return null;
        }
        int authorJsonStart = authorLine.indexOf("{");
        int authorJsonEnd = authorLine.lastIndexOf("}");

        if (authorJsonStart == -1 || authorJsonEnd == -1) {
            System.out.println("Open or closed brace not found for Author json");
            return null;
        }

        String authorJsonString = authorLine.substring(authorJsonStart, authorJsonEnd + 1);

        try {
            JSONObject jsonObject = new JSONObject(authorJsonString);

            String authorName = jsonObject.optString("name", "#");
            String personalName = jsonObject.optString("personal_name", "#");
            String key = jsonObject.optString("key", "#");

            String authorId;

            if (key.equals("#")) {
                System.out.println("Author Id not found! Returning null!");
                return null;
            } else {
                int lastSlash = key.lastIndexOf("/");
                if (lastSlash < 0 || lastSlash + 1 == key.length()) {
                    System.out.println("Author Id not found! Returning null!");
                    return null;
                }
                authorId = key.substring(lastSlash + 1).trim();
                if (authorId.isEmpty()) {
                    System.out.println("Author Id not found! Returning null!");
                    return null;
                }
            }

            AuthorById author = new AuthorById();
            author.setAuthorId(authorId);
            author.setAuthorName(authorName);
            author.setPersonalName(personalName);

            return author;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
