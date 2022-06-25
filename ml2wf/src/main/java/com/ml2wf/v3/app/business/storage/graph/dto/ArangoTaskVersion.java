package com.ml2wf.v3.app.business.storage.graph.dto;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.PersistentIndex;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Document("Versions")
@PersistentIndex(fields = { "name" })
public class ArangoTaskVersion {

    @Id
    private String id;
    @ArangoId
    private String arangoId;
    private int major;
    private int minor;
    private int patch;
    private String name;

    public ArangoTaskVersion(int major, int minor, int patch, String name) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.name = name;
    }
}
