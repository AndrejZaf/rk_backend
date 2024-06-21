#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "postgres" <<-EOSQL
    CREATE DATABASE rk_inventory;
    CREATE DATABASE rk_payment;
    CREATE DATABASE rk_order;
EOSQL