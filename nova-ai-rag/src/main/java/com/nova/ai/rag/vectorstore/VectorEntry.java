package com.nova.ai.rag.vectorstore;

public final class VectorEntry {
    private final String chunkId;
    private final float[] embedding;
    private final String knowledgeBaseId;
    private final String content;

    public VectorEntry(String chunkId, float[] embedding, String knowledgeBaseId, String content) {
        this.chunkId = chunkId;
        this.embedding = embedding;
        this.knowledgeBaseId = knowledgeBaseId;
        this.content = content;
    }

    public String chunkId() {
        return chunkId;
    }

    public float[] embedding() {
        return embedding;
    }

    public String knowledgeBaseId() {
        return knowledgeBaseId;
    }

    public String content() {
        return content;
    }
}
