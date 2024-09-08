CREATE TRIGGER counter_increment
    BEFORE INSERT
    ON public.chat
    FOR EACH ROW
    EXECUTE PROCEDURE public.insert_row();