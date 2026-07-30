package org.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageContent {
    private String text;
    private String url;
}
