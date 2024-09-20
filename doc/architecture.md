# Architecture

## General

**dls** achieves its high availability goal by using multiple simple locks. A lock is considered successfull from user point of view when a quorum number of locks are successful. This way, the system is not affected in case a number of locks are not successfull due to various reasons - network issues, api issues, db issues, storage issues, etc.

## Boundary conditions

For the sake of simplicity, first version of DLS executes sublocks consequtively. While this brings in slowness by design, it makes later processing of sublock results easier. Since this in itself is not so simple, any simplification is welcome at the moment.

Once processing of sublock results asuming consequtive execution is final and stable, changing the execution of the sublocks to parallel can be considered.

## Scenarios

This section will list the expected combinations of sublock results and how they will interpreted.

Sublock results could be one of the following:

- OK - sublock acquired;
- ALREADY_LOCKED - sublock already acquired by someone else;
- FAILED - sublock operation failed due to infrastructure issues;

Having the possible sublock results listed above, the following scenarios are considered (sorted by expected probability):

| Sublock results       | Lock result       | Post process  |
| ---                   | ---               | ---           |
| all OK                | OK                |               |
| all ALREADY_LOCKED    | ALREADY_LOCKED    |               |
| quorum OK             | OK                | Unify         |
| quorum ALREADY_LOCKED | ALREADY_LOCKED    | Cleanup       |
| quorum (FAILED + ALREADY_LOCKED)  | FAILED| Cleanup       |
| some OK               | TBD               | Compensate    |
| all FAILED            | FAILED            |               |

### Unify

### Cleanup

### Compensate

## Building blocks

- API
- Sublock API
- Sublock DB
- Message broker
- Reconciler

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
