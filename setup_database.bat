@echo off
echo ========================================
echo VerifyDocs Database Setup
echo ========================================
echo.

echo Creating database verifydocs_db...
psql -U postgres -c "DROP DATABASE IF EXISTS verifydocs_db;"
psql -U postgres -c "CREATE DATABASE verifydocs_db;"

echo.
echo Database created successfully!
echo.
echo Running schema and sample data...
psql -U postgres -d verifydocs_db -f database_schema.sql

echo.
echo ========================================
echo Database setup complete!
echo ========================================
pause
