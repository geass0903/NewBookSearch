package jp.gr.java_conf.nuranimation.new_book_search;

import jp.gr.java_conf.nuranimation.new_book_search.service.NewBookService;

public enum FragmentEvent {

    START_RELOAD_NEW_BOOKS {
        @Override
        public void apply(MainActivity activity) {
            NewBookService service = activity.getService();
            service.startReloadNewBooks();
        }
    },

    STOP_RELOAD_NEW_BOOKS {
        @Override
        public void apply(MainActivity activity) {
            NewBookService service = activity.getService();
            service.stopReloadNewBooks();
        }
    },

    START_BACKUP_DROPBOX {
        @Override
        public void apply(MainActivity activity) {
            NewBookService service = activity.getService();
            service.startBackupDropbox();
        }
    },

    STOP_BACKUP_DROPBOX {
        @Override
        public void apply(MainActivity activity) {
            NewBookService service = activity.getService();
            service.stopBackupDropbox();
        }
    },

    START_RESTORE_DROPBOX {
        @Override
        public void apply(MainActivity activity) {
            NewBookService service = activity.getService();
            service.startRestoreDropbox();
        }
    },

    STOP_RESTORE_DROPBOX {
        @Override
        public void apply(MainActivity activity) {
            NewBookService service = activity.getService();
            service.stopRestoreDropbox();
        }
    },

    @SuppressWarnings("unused")
    DEFAULT {
        @Override
        public void apply(MainActivity activity) {
        }
    };

    abstract public void apply(MainActivity activity);

}
