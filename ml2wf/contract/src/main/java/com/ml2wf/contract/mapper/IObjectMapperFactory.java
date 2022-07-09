package com.ml2wf.contract.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface IObjectMapperFactory {

    ObjectMapper createNewObjectMapper();
}
