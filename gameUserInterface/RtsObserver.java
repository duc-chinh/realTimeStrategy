package realTimeStrategy.gameUserInterface;

import java.util.List;

public interface RtsObserver
{
	public void notify(List<RtsEvent> events);
}
