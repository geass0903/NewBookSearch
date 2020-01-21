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


    @SuppressWarnings("unused")
    DEFAULT {
        @Override
        public void apply(MainActivity activity) {
        }
    };

    abstract public void apply(MainActivity activity);

}
