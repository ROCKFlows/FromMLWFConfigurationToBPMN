package com.ml2wf.arango.storage.converter;

import com.ml2wf.arango.storage.dto.ArangoTaskVersion;
import com.ml2wf.contract.storage.graph.converter.IGraphVersionConverter;
import org.springframework.stereotype.Component;

@Component
public interface IArangoVersionConverter extends IGraphVersionConverter<ArangoTaskVersion> {

}
