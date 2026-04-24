package com.nova.ai.rag.embedder;

/**
 * Embedding service for converting text to vector representations.
 * In production, this would call an embedding model (e.g., OpenAI text-embedding-ada-002).
 */
public class EmbeddingService {

    private static final int EMBEDDING_DIM = 1536;

    public float[] embed(String text) {
        float[] embedding = new float[EMBEDDING_DIM];
        int hash = text.hashCode();
        for (int i = 0; i < EMBEDDING_DIM; i++) {
            embedding[i] = (float) Math.sin(hash + i * 0.1);
        }
        return embedding;
    }

    public int getDimension() {
        return EMBEDDING_DIM;
    }
}
