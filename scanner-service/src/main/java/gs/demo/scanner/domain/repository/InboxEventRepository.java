package gs.demo.scanner.domain.repository;

public interface InboxEventRepository {

    Boolean alreadyHandled(String eventId);
}
