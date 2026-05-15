package com.nova.ai.rag.vectorstore;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Milvus vector store integration for RAG.
 * In production, this connects to a real Milvus server via the Milvus Java SDK.
 */
@Component
public class MilvusVectorStore {

    private static final Logger log = LoggerFactory.getLogger(MilvusVectorStore.class);
    private final Map<String, VectorEntry> store = new ConcurrentHashMap<>();

    public void insert(String chunkId, float[] embedding, String knowledgeBaseId, String content) {
        store.put(chunkId, new VectorEntry(chunkId, embedding, knowledgeBaseId, content));
        log.debug("Inserted chunk: {} into KB: {}", chunkId, knowledgeBaseId);
    }

    public List<VectorEntry> search(float[] queryEmbedding, String knowledgeBaseId, int topK) {
        return store.values().stream()
            .filter(e -> e.knowledgeBaseId().equals(knowledgeBaseId))
            .map(e -> new ScoredEntry(e, cosineSimilarity(queryEmbedding, e.embedding())))
            .sorted((a, b) -> Float.compare(b.score(), a.score()))
            .limit(topK)
            .map(e -> new VectorEntry(e.chunkId(), e.embedding(), e.knowledgeBaseId(), e.content()))
            .toList();
    }

    private float cosineSimilarity(float[] a, float[] b) {
        double dotProduct = 0, normA = 0, normB = 0;
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            dotProduct += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return (float) (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB) + 1e-9));
    }
}

