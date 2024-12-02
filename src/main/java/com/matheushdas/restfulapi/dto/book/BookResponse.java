package com.matheushdas.restfulapi.dto.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.Objects;

@JsonPropertyOrder({"id", "title", "author", "launch_date", "price", "_links"})
public class BookResponse extends RepresentationModel<BookResponse> {
    @JsonProperty("id")
    private Long key;

    @JsonProperty("title")
    private String title;

    @JsonProperty("author")
    private String author;

    @JsonProperty("launch_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date launchDate;

    @JsonProperty("price")
    private Double price;

    public BookResponse() {
    }

    public BookResponse(Long key, String title, String author, Date launchDate, Double price) {
        this.key = key;
        this.title = title;
        this.author = author;
        this.launchDate = launchDate;
        this.price = price;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookResponse that = (BookResponse) o;
        return Objects.equals(key, that.key) && Objects.equals(title, that.title) && Objects.equals(author, that.author) && Objects.equals(launchDate, that.launchDate) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, title, author, launchDate, price);
    }
}
