package com.asiainfo.knowledge.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextChunkVO {
    private String paragraphId;
    private Float score;
}
