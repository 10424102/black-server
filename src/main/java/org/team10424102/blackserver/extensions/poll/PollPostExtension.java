package org.team10424102.blackserver.extensions.poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.team10424102.blackserver.extensions.PostExtension;
import org.team10424102.blackserver.extensions.PostExtensionIdentifier;

@PostExtensionIdentifier("poll")
@Component
public class PollPostExtension implements PostExtension {

    @Autowired PollRepo pollRepo;

    @Override
    public Object getData(String stub) {
        Long id = Long.parseLong(stub);
        Poll poll = pollRepo.findOne(id);
        return new PollResult(poll);
    }
}
