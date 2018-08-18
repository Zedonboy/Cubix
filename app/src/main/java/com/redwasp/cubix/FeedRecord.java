package com.redwasp.cubix;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "feedTable",
        indexes = {
                @Index(value = "title", unique = false),
                @Index(value = "searchUrl", unique = false)
        }
)
public class FeedRecord {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "title")
    private String title;

    @Property
    private String body;

    @Property
    private String searchUrl;

@Generated(hash = 1548585855)
public FeedRecord(Long id, String title, String body, String searchUrl) {
    this.id = id;
    this.title = title;
    this.body = body;
    this.searchUrl = searchUrl;
}

@Generated(hash = 1734409969)
public FeedRecord() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getTitle() {
    return this.title;
}

public void setTitle(String title) {
    this.title = title;
}

public String getBody() {
    return this.body;
}

public void setBody(String body) {
    this.body = body;
}

public String getSearchUrl() {
    return this.searchUrl;
}

public void setSearchUrl(String searchUrl) {
    this.searchUrl = searchUrl;
}
}
