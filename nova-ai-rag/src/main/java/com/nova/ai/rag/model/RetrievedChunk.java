package com.nova.ai.rag.model;

/**
 * Single chunk retrieved from the vector store for RAG.
 */
public class RetrievedChunk {

    private String chunkId;

    private String content;

    private double score;

    public RetrievedChunk() {
    }

    public RetrievedChunk(String chunkId, String content, double score) {
        this.chunkId = chunkId;
        this.content = content;
        this.score = score;
    }

    public String getChunkId() {
        return chunkId;
    }

    public void setChunkId(String chunkId) {
        this.chunkId = chunkId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
