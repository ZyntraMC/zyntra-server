package mc.zyntra.general.server.load.balancer.element;

public interface LoadBalancerObject {
   String getServerId();

   long getStartTime();

   boolean canBeSelected();
}
