import {
  configureStore,
  Reducer,
  AnyAction,
  ThunkAction,
  Action,
  CombinedState,
  combineReducers,
} from '@reduxjs/toolkit';
import { HYDRATE, createWrapper } from 'next-redux-wrapper';

// 리듀서 State 타입 정의
export interface ReducerStates {}

// 루트 리듀서 생성
const rootReducer = (
  state: ReducerStates,
  action: AnyAction
): CombinedState<ReducerStates> => {
  switch (action.type) {
    // next-redux-wrapper의 HYDRATE 액션 처리
    case HYDRATE:
      return action.payload;

    // 슬라이스 통합
    default: {
      const combinedReducer = combineReducers({
        // 리듀서 넣어주기
      });
      return combinedReducer(state, action);
    }
  }
};

// store 생성 함수
const makeStore = () => {
  const store = configureStore({
    reducer: rootReducer as Reducer<ReducerStates, AnyAction>, // 리듀서
    devTools: process.env.NODE_ENV === 'development', // 개발자도구
  });

  return store;
};

// 타입 익스포트
export type AppStore = ReturnType<typeof makeStore>; // store 타입
export type RootState = ReturnType<typeof rootReducer>; // RootState 타입
export type AppDispatch = AppStore['dispatch']; // dispatch 타입

// next-redux-wrapper의 wrapper 생성
const wrapper = createWrapper<AppStore>(makeStore, {
  debug: process.env.NODE_ENV === 'development',
});

// wrapper 익스포트
export default wrapper;
