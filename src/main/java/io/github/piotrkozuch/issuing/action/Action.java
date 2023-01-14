package io.github.piotrkozuch.issuing.action;

import java.util.Optional;

public interface Action<RQ, RS> {

    Optional<RS> execute(RQ params);
}
