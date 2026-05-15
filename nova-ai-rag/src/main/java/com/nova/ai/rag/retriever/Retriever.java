package com.nova.ai.rag.retriever;

import com.nova.ai.rag.model.RetrievedChunk;
import com.nova.ai.rag.vectorstore.MilvusVectorStore;
import com.nova.ai.rag.vectorstore.VectorEntry;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * Retriever for RAG pipeline.
 * Performs similarity search in Milvus vector store.
 */
@Component
public class Retriever {

    private final MilvusVectorStore vectorStore;

    public Retriever(MilvusVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<RetrievedChunk> retrieve(float[] queryEmbedding, String knowledgeBaseId, int topK) {
        List<VectorEntry> results = vectorStore.search(queryEmbedding, knowledgeBaseId, topK);
        return results.stream()
            .map(r -> new RetrievedChunk(r.chunkId(), r.content(), 1.0))
            .toList();
    }
}
