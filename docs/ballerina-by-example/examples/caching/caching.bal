import ballerina/caching;
import ballerina/io;
import ballerina/runtime;

public function main (string[] args) {
    // Create a new cache. The cache cleanup task runs every 5 seconds and clears
    // any expired cache. To test cache cleaning, the cache 
    // expiry time is set to 4 seconds here.
    caching:Cache cache = caching:createCache("UserCache", 4000, 10, 0.1);
    // Add a new entry to the cache.
    cache.put("Name", "Ballerina");
    // Get the cached value.
    string name;
    if (cache.hasKey("Name")){
        name = <string>cache.get("Name");
    }
    io:println("Name: " + name);

    // Send the current worker to sleep mode for 6 seconds. No execution takes place during this period.
    runtime:sleepCurrentWorker(6000);
    // Since the cache expiry time is 4 seconds, the cache cleanup task runs at the 5th
    // second and cleans the cache while this thread is sleeping. Now, this value
    // is null.

    if (cache.hasKey("Name")){
        name = <string>cache.get("Name");
    } else {
        name = "";
    }
    io:println("Name: " + name);
}
