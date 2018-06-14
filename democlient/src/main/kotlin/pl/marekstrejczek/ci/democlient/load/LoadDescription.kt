package pl.marekstrejczek.ci.democlient.load

data class LoadDescription(val loadId: String, val perSecondRequestCount: Int, val totalRequests: Int)