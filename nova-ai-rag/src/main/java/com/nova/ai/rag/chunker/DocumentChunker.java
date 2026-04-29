package com.nova.ai.rag.chunker;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Document chunker for RAG pipeline.
 * Splits documents into overlapping chunks of specified token size.
 */
@Component
public class DocumentChunker {

    private static final int DEFAULT_OVERLAP = 50;

    public List<String> chunk(String content, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        if (content == null || content.isEmpty()) {
            return chunks;
        }

        String[] words = content.split("\\s+");
        int overlap = Math.min(DEFAULT_OVERLAP, chunkSize / 4);

        for (int i = 0; i < words.length; i += chunkSize - overlap) {
            int end = Math.min(i + chunkSize, words.length);
            StringBuilder chunk = new StringBuilder();
            for (int j = i; j < end; j++) {
                chunk.append(words[j]).append(" ");
            }
            chunks.add(chunk.toString().trim());
        }

        return chunks;
    }
}
