package io.harshit.betterreadsv2dataloader.book;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class BookParser {

    public BookById getBookByIdFromLine(String bookLine) {
        if (bookLine == null || bookLine.isEmpty()) {
            return null;
        }

        int bookJsonStart = bookLine.indexOf("{");
        int bookJsonEnd = bookLine.lastIndexOf("}");

        if (bookJsonStart == -1 || bookJsonEnd == -1) {
            System.out.println("Open or closed brace not found for Work json");
            return null;
        }

        String bookJsonString = bookLine.substring(bookJsonStart, bookJsonEnd + 1);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            JSONObject bookJSONObject = new JSONObject(bookJsonString);

            String id = bookJSONObject.optString("key", "#").replace("/works/", "");
            String name = bookJSONObject.optString("title", "#");

            String description;
            JSONObject descriptionObject = bookJSONObject.optJSONObject("description");
            if (Objects.nonNull(descriptionObject)) {
                description = descriptionObject.optString("value", "#");
            } else {
                description = "#";
            }

            JSONArray coversJSONArray = bookJSONObject.optJSONArray("covers");
            List<String> coverIds = new ArrayList<>();
            if (Objects.nonNull(coversJSONArray)) {
                int coverJSONArrayLen = coversJSONArray.length();
                for (int i = 0; i < coverJSONArrayLen; i++) {
                    coverIds.add(coversJSONArray.getString(i));
                }
            }

            JSONArray authorsJSONArray = bookJSONObject.optJSONArray("authors");
            List<String> authorIds = new ArrayList<>();
            if (Objects.nonNull(authorsJSONArray)) {
                int authorJSONArrayLen = authorsJSONArray.length();
                for (int i = 0; i < authorJSONArrayLen; i++) {
                    authorIds.add("#");
                    JSONObject authorArrayElement = authorsJSONArray.optJSONObject(i);
                    if (Objects.nonNull(authorArrayElement)) {
                        JSONObject authorObject = authorArrayElement.optJSONObject("author");
                        if (Objects.nonNull(authorObject)) {
                            String authorId = authorObject.optString("key", "#").replace("/authors/", "");
                            authorIds.set(i, authorId);
                        }
                    }
                }
            }

            LocalDate publishedDate = null;
            JSONObject createdObject = bookJSONObject.optJSONObject("created");
            if (Objects.nonNull(createdObject)) {
                String dateString = createdObject.optString("value");
                if (Objects.nonNull(dateString)) {
                    dateString = dateString.substring(0, dateString.indexOf("T"));
                    publishedDate = LocalDate.parse(dateString, dateTimeFormatter);
                }
            }

            BookById bookById = new BookById();

            bookById.setBookId(id);
            bookById.setBookName(name);
            bookById.setDescription(description);
            bookById.setCoverIds(coverIds);
            bookById.setAuthorIds(authorIds);
            bookById.setPublishedDate(publishedDate);

            return bookById;
        } catch (JSONException e) {
            System.out.println("---- JSONException thrown ----");
            e.printStackTrace();
        }
        return null;
    }

}
