package com.ml2wf.v2.tree;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode
@SuperBuilder
@NoArgsConstructor
public abstract class AbstractTree<T> implements ITreeManipulable<T> {

}
