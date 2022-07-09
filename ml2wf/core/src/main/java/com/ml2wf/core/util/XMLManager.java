package com.ml2wf.core.util;

import com.ml2wf.core.conventions.Notation;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XMLManager { // TODO: to rename

    /**
     * Returns an {@code Optional} containing the referred meta task from the given
     * {@code reference} text.
     *
     * @param reference reference containing the referred meta task
     *
     * @return an {@code Optional} containing the referred meta task from the given
     *         {@code reference} text
     */
    public static Optional<String> getReferredTask(String reference) {
        String regex = String.format("%s(\\w*)", Notation.REFERENCE_VOC);
        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(reference);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }
        return Optional.empty();
    }
}
