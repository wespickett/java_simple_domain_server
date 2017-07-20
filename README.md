# Java Simple Domain Server

A simple Java server to implement your domain logic. 

Connects to your application server via socket. I use socket.io on nodejs.

The domain server expects a message like:

```
{
    resource: 'SOME_RESOURCE_ENTITY',
    action: 'GET',
    data: <json_object>
}
```


