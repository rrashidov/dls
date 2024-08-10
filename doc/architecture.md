# Architecture

## General

**dls** achieves its high availability goal by using multiple simple locks. A lock is considered successfull from user point of view when a quorum number of locks are successful. This way, the system is not affected in case a number of locks are not successfull due to various reasons - network issues, api issues, db issues, storage issues, etc.

## Building blocks

- API
- Sublock API
- Sublock DB
- Message broker
- Reconciler

## Scenarios

## Failure scenarios

## API

```
POST /v1/lock/{lockid}
```

This endpoint is used to acquire a lock.

```
DELETE /v1/lock/{lockid}
```

This endpoint is used to release an acquired lock.

```
GET /v1/lock/{lockid}
```

This endpoint is used to check if a lock exists or not.
