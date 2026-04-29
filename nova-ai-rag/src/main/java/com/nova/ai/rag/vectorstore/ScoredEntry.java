package com.nova.ai.rag.vectorstore;

public final class ScoredEntry {
    private final String chunkId;
    private final float[] embedding;
    private final String knowledgeBaseId;
    private final String content;
    private final float score;

    public ScoredEntry(String chunkId, float[] embedding, String knowledgeBaseId, String content, float score) {
        this.chunkId = chunkId;
        this.embedding = embedding;
        this.knowledgeBaseId = knowledgeBaseId;
        this.content = content;
        this.score = score;
    }

    ScoredEntry(VectorEntry entry, float score) {
        this(entry.chunkId(), entry.embedding(), entry.knowledgeBaseId(), entry.content(), score);
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

    public float score() {
        return score;
    }
}
