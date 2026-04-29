package com.nova.ai.rag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RetrievedChunk {
    private String chunkId;

    private String content;

    private double score;

}
