package com.nova.ai.rag.service;

import com.nova.ai.rag.chunker.DocumentChunker;
import com.nova.ai.rag.embedder.EmbeddingService;
import com.nova.ai.rag.model.RetrievedChunk;
import com.nova.ai.rag.vectorstore.MilvusVectorStore;
import com.nova.ai.rag.retriever.Retriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RAG (Retrieval Augmented Generation) Service.
 * Flow: Document -> Chunk -> Embed -> Store in Milvus -> Retrieve -> Augment Prompt
 */
@Service
public class RagService {

    private static final Logger log = LoggerFactory.getLogger(RagService.class);

    private final DocumentChunker chunker;
    private final EmbeddingService embeddingService;
    private final MilvusVectorStore vectorStore;
    private final Retriever retriever;

    public RagService(DocumentChunker chunker, EmbeddingService embeddingService,
                      MilvusVectorStore vectorStore, Retriever retriever) {
        this.chunker = chunker;
        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
        this.retriever = retriever;
    }

    public void ingestDocument(String documentId, String content, String knowledgeBaseId) {
        log.info("Ingesting document: {} into knowledge base: {}", documentId, knowledgeBaseId);

        List<String> chunks = chunker.chunk(content, 500);
        log.info("Document split into {} chunks", chunks.size());

        for (int i = 0; i < chunks.size(); i++) {
            String chunkId = documentId + "-chunk-" + i;
            float[] embedding = embeddingService.embed(chunks.get(i));
            vectorStore.insert(chunkId, embedding, knowledgeBaseId, chunks.get(i));
        }

        log.info("Document ingestion completed: {}", documentId);
    }

    public List<RetrievedChunk> retrieve(String query, String knowledgeBaseId, int topK) {
        log.info("Retrieving knowledge for query: {} from KB: {}", query, knowledgeBaseId);

        float[] queryEmbedding = embeddingService.embed(query);
        return retriever.retrieve(queryEmbedding, knowledgeBaseId, topK);
    }

    public String augmentPrompt(String userQuery, String knowledgeBaseId) {
        List<RetrievedChunk> chunks = retrieve(userQuery, knowledgeBaseId, 5);
        StringBuilder context = new StringBuilder();
        for (RetrievedChunk chunk : chunks) {
            context.append(chunk.getContent()).append("\n---\n");
        }
        return "Based on the following knowledge:\n" + context + "\nAnswer: " + userQuery;
    }
}
