-- Fix verification_logs table to support longer result messages and nullable document_id

-- Step 1: Alter result column to support longer messages
ALTER TABLE verification_logs 
ALTER COLUMN result TYPE VARCHAR(500);

-- Step 2: Make document_id nullable (for failed verifications where document doesn't exist)
ALTER TABLE verification_logs 
ALTER COLUMN document_id DROP NOT NULL;

-- Verify changes
SELECT column_name, data_type, character_maximum_length, is_nullable 
FROM information_schema.columns 
WHERE table_name = 'verification_logs';
