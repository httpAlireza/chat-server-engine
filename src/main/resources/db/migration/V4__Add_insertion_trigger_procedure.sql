CREATE OR REPLACE FUNCTION public.insert_row()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
DECLARE
max_num bigint;
BEGIN
SELECT max(message_number) as maximum into max_num from chat where unique_chat = NEW."unique_chat";
IF max_num IS NOT NULL THEN
    		NEW."message_number" = (max_num+1);
END IF;
RETURN NEW;
END;
$$

