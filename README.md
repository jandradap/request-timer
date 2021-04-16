# Request Timer

Application to make http requests and takes to respond the time (in milliseconds) that you indicate by parameter.

## API

### Http request fixed time

`GET /request-timer/v1/api/timer/fixed/{time}`.

- Description: http request that waits for a fixed amount of time passed by parameter.
- Parameters:
  - time : Time in milliseconds
- Example:
  - /request-timer/v1/api/timer/fixed/1000

### Http request with random time

`GET /request-timer/v1/api/timer/rand/{min}/{max}`
- Description: http request that waits for a random time between the values passed by parameter.
- Parameters:
  - min : Minimum time in milliseconds
  - max : Maximum time in milliseconds
- Example:
  - /request-timer/v1/api/timer/rand/1000/10000

## How to deploy to OpenShift

You should have your environment ready:

- Get `Maven` installed and configured
- Get `oc` client installed and logged to your OpenShift Cluster

1. Clone this repo:
    ```
    git clone git@github.com:josgonza-rh/request-timer.git && \
    cd request-timer
    ```

2. Create the project:
    ```
    export PROJECT=request-timer &&
    oc new-project ${PROJECT}
    ```
    
3. Deploy it:
    ```
    mvn clean oc:deploy -DskipTests -Popenshift
    ```

## Apache HTTP server benchmarking tool

[ab](https://httpd.apache.org/docs/2.4/programs/ab.html) is a tool for benchmarking your Apache Hypertext Transfer Protocol (HTTP) server. It is designed to give you an impression of how your current Apache installation performs. This especially shows you how many requests per second your Apache installation is capable of serving.

`ab` is available for Linux, Windows, .. for example, you can get it installed in your Fedora as follows:

```
sudo dnf install httpd-tools
```

Runing your http benchmark (just an example):

1. Pre-req:
    ```
    ulimit -n 20000
    oc scale --replicas=40 deploymentconfig request-timer
    ```
    > NOTE: to avoid your environment be limited in order to run the benchmark.

2. Then, launch the test:
    ```
    ab -k -n 20000 -c 1500 https://<your-host>/request-timer/v1/api/timer/fixed/100
    ```

A sample output for a small test looks like follows:

```
$ ab -k -n 100 -c 10 https://<your-host>/request-timer/v1/api/timer/fixed/100

This is ApacheBench, Version 2.3 <$Revision: 1879490 $>
Copyright 1996 Adam Twiss, Zeus Technology Ltd, http://www.zeustech.net/
Licensed to The Apache Software Foundation, http://www.apache.org/

Benchmarking <your-host> (be patient).....done


Server Software:        
Server Hostname:        <your-host>
Server Port:            443
SSL/TLS Protocol:       TLSv1.2,ECDHE-RSA-AES128-GCM-SHA256,2048,128
Server Temp Key:        X25519 253 bits
TLS Server Name:        <your-host>

Document Path:          /request-timer/v1/api/timer/fixed/100
Document Length:        3265 bytes

Concurrency Level:      10
Time taken for tests:   7.073 seconds
Complete requests:      100
Failed requests:        0
Non-2xx responses:      100
Keep-Alive requests:    0
Total transferred:      341800 bytes
HTML transferred:       326500 bytes
Requests per second:    14.14 [#/sec] (mean)
Time per request:       707.266 [ms] (mean)
Time per request:       70.727 [ms] (mean, across all concurrent requests)
Transfer rate:          47.19 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:      443  486  42.7    469     590
Processing:   146  156  16.3    150     208
Waiting:      146  156  16.3    149     208
Total:        590  642  43.2    622     740

Percentage of the requests served within a certain time (ms)
  50%    622
  66%    643
  75%    657
  80%    682
  90%    723
  95%    726
  98%    739
  99%    740
 100%    740 (longest request)
```

> NOTE: the test is in a local cluster with just one pod serving all the requests.