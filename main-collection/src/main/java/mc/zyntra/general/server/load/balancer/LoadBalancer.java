package mc.zyntra.general.server.load.balancer;

import mc.zyntra.general.server.load.balancer.element.LoadBalancerObject;

public interface LoadBalancer<T extends LoadBalancerObject> {
   T next();
}
