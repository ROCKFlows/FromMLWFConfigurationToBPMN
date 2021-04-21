package com.ml2wf.v2.tree;

public interface ITreeManipulable<T> {

    T appendChild(T child);

    T removeChild(T child);
}
